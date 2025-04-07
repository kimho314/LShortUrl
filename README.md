# URL 단축기

## 요구 사항

### 기본 기능

1. URL 단축 : 주어진 긴 URL을 훨씬 짧게 줄인다.
2. URL redirection : 축약된 url로 http 요청이 오면 원래 url로 안내한다.
3. 높은 규모 확장성 그리고 장애 감내가 요구된다.

### 계략적 추정

1. 쓰기 연산 : 매일 1억 개의 단축 url 생성
2. 초당 쓰기 연산 : 1억/24/3600 = 1160
3. 읽기 연산 : 읽기 연산과 쓰기 연산 비율은 10:1이라고 하자. 그 경우 읽기 연산은 초당 11,600회 발생한다.
4. URL 단축 서비스를 10년간 운영한다고 가정하면 1억 / 365 / 10 = 3650억개의 레코드를 보관해야 한다.
5. 축약 전 url의 평균 길이를 100이라고 하자. 따라서 10년 동안 필요한 저장 용ㅇ량은 3650억 * 100바이트 = 36.5TB 이다.

### API endpoint

1. url 단축용 endpoint : 새 단축 url 생성

```
POST /api/v1/data/shorten
- request : {longUrl : String}
- response : 단축 url
```

2. url redirect endpoint : 단축 url에 대해서 http요청이 오면 원래 url로 보내주기 위한 용도의 endpoint

```
GET /api/v1/{shortUrl}
- response : HTTP redirection 목적지가 될 원래 url
```

### ERD

```mermaid
erDiagram
    shorten_url{
        String id
        Stirng short_url
        String long_url
    }
```

## STEP1
처음은 가장 단순한 아키텍쳐인 웹 서버와 RDBMS로 시작한다.
RDBMS로는 가장 인기많은 벤더 중 하나인 MySQL을 골랐다.

![Alt text here](shorturl1.png)

### stress load test (write)
첫번째로 쓰기 작업인 `/data/shorten` API에 대해서 부하 테스트를 실행 하였다. 

- hikari 설정
  - maximum-pool-size: 20
  - conection-timeout: 5000
  - max-lifetime: 50000
  - idle-timeout: 50000

- k6 설정
  - vus: 100
  - duration: 30s

![Alt text here](vus100_write_http_performance.png)

![Alt text here](vus100_write_http1.png)

결과: db connection pool size를 20으로 설정했음에도 100명의 동시 접속자의 쓰기/읽기 작업이 가능한다는 것을 알았다. connectoin-pool-size보다 동시 접속자 수가 많더라도 쓰기/읽기 성능이 빠르면 pool size를 동시 접속자 수만큼 늘이지 않아도 충분히 처리할 수 있다는 점을 알 수 있다.


- hikari 설정
  - maximum-pool-size: 20
  - conection-timeout: 5000
  - max-lifetime: 50000
  - idle-timeout: 50000

- tomcat 설정
  - thread.max: 200 # thread pool에서 아용할 최대 스레드 갯수
  - thread.min-spare: 10 # thread pool에서 최소한으로 유지할 스레드 갯수
  - accept-count: 100 # max-connections 이상의 요청이 들어왔을 때 사용하는 요청 대기열 queue의 사이즈
  - max-connections: 8192 # 동시에 처리할 수 있는 최대 connection의 갯수
  - connection-timeout: 60000

- k6 설정
  - vus: 500

![Alt text here](vus500_write.png)

결과: 0.16%의 http request가 실패 한 것을 확인 하였습니다. 실패율을 0%로 맞추기 위해 thread.max를 vu 갯수와 동일한 500으로 조정하고 다시 테스트를 수행 하였습니다.

- hikari 설정
  - maximum-pool-size: 20
  - conection-timeout: 5000
  - max-lifetime: 50000
  - idle-timeout: 50000

- tomcat 설정
  - thread.max: 500
  - thread.min-spare: 10
  - accept-count: 100 
  - max-connections: 8192 
  - connection-timeout: 60000

- k6 설정
  - vus: 500

![Alt text here](vus500_write2.png)

결과: 여전히 실패한 http request가 있어서(0.13%) 로그를 확인 해보니 HikariPool에서 connection pool이 모자라서 waiting하는것이 보여서 hikari.maximum-pool-size를 닐려서 다시 테스트 수행하였습니다.

- hikari 설정
  - maximum-pool-size: 50
  - conection-timeout: 5000
  - max-lifetime: 50000
  - idle-timeout: 50000

- tomcat 설정
  - thread.max: 500 
  - thread.min-spare: 10 
  - accept-count: 100 
  - max-connections: 8192 
  - connection-timeout: 60000

- k6 설정
  - vus: 500

![Alt text here](vus500_write3.png)

결과: 여전히 request fail이 있지만 실패율이 0.08%로 약 38% 줄어든것을 확인 할 수 있었다.


- hikari 설정
  - maximum-pool-size: 60
  - conection-timeout: 5000
  - max-lifetime: 50000
  - idle-timeout: 50000

- tomcat 설정
  - thread.max: 500 
  - thread.min-spare: 10
  - accept-count: 100 
  - max-connections: 8192 
  - connection-timeout: 60000

- k6 설정
  - vus: 500

  ![Alt text here](vus500_write4.png)

  결과: http request 실패율이 0%가 되었고, TPS = 474로 나온 것을 확인하였다.

  - hikari 설정
  - maximum-pool-size: 300
  - conection-timeout: 5000
  - max-lifetime: 1800000
  - idle-timeout: 50000

- tomcat 설정
  - thread.max: 1000 
  - thread.min-spare: 10
  - accept-count: 100 
  - max-connections: 8192 
  - connection-timeout: 60000

- k6 설정
  - stages: [
    {duration: '5s', target: 3000},
    {duration: '10s', target: 0}
  ]

![Alt text here](vus3000_write5.png)

결과: tomcat thread 갯수와 hikaricp의 maximum-pool-size를 모두 상향시켜서 tps 1439가 나온것을 확인 하였다.

### stress load test (read)
두번째로 읽기 작업인 `/{shortUrl}` API에 대해서 부하 테스트를 실행 하였다.

- hikari 설정
  - maximum-pool-size: 20
  - conection-timeout: 5000
  - max-lifetime: 50000
  - idle-timeout: 50000

- tomcat 설정
  - thread.max: 200 
  - thread.min-spare: 10
  - accept-count: 100 
  - max-connections: 8192 
  - connection-timeout: 60000

- k6 설정
  - vus: 100
  - duration: 30s



![Alt text here](vus100_read_http_performance.png)

![Alt text here](vus100_read_http1.png)


- hikari 설정
  - maximum-pool-size: 20
  - conection-timeout: 5000
  - max-lifetime: 50000
  - idle-timeout: 50000

- tomcat 설정
  - thread.max: 200 
  - thread.min-spare: 10
  - accept-count: 100 
  - max-connections: 8192 
  - connection-timeout: 60000

- k6 설정
  - stages: [
    {duration: '5s', target: 500},
    {duration: '10s', target: 0}
  ]

![Alt text here](vus500_read2.png)

- hikari 설정
  - maximum-pool-size: 20
  - conection-timeout: 5000
  - max-lifetime: 50000
  - idle-timeout: 50000

- tomcat 설정
  - thread.max: 200 
  - thread.min-spare: 10
  - accept-count: 100 
  - max-connections: 8192 
  - connection-timeout: 60000

- k6 설정
  - stages: [
    {duration: '5s', target: 1000},
    {duration: '10s', target: 0}
  ]

![Alt text here](vus1000_read3.png)

- hikari 설정
  - maximum-pool-size: 20
  - conection-timeout: 5000
  - max-lifetime: 50000
  - idle-timeout: 50000

- tomcat 설정
  - thread.max: 200 
  - thread.min-spare: 10
  - accept-count: 100 
  - max-connections: 8192 
  - connection-timeout: 60000

- k6 설정
  - stages: [
    {duration: '5s', target: 2000},
    {duration: '10s', target: 0}
  ]

![Alt text here](vus2000_read4.png)

- hikari 설정
  - maximum-pool-size: 100
  - conection-timeout: 5000
  - max-lifetime: 50000
  - idle-timeout: 50000

- tomcat 설정
  - thread.max: 200
  - thread.min-spare: 10
  - accept-count: 100 
  - max-connections: 8192 
  - connection-timeout: 60000

- k6 설정
  - stages: [
    {duration: '5s', target: 2000},
    {duration: '10s', target: 0}
  ]

  ![Alt text here](vus2000_read5.png)

  - hikari 설정
  - maximum-pool-size: 100
  - conection-timeout: 5000
  - max-lifetime: 50000
  - idle-timeout: 50000

- tomcat 설정
  - thread.max: 400
  - thread.min-spare: 10
  - accept-count: 100 
  - max-connections: 8192 
  - connection-timeout: 60000

- k6 설정
  - stages: [
    {duration: '5s', target: 2000},
    {duration: '10s', target: 0}
  ]

  ![Alt text here](vus2000_read6.png)

  결과: vu의 갯수를 2000까지 늘려도 TPS가 1700까지 상승 후 오르지 않았다. grafana를 통해 확인 해보니 db connection이 maximum pool size를 넘지는 않아서 maximum tomcat thread의 갯수를 늘려서 테스트 해보니 웹 서버의 cpu부하가 높아져서 http request가 실패나는 것을 확인 하였다. maximum tomcat thread 갯수를 적당히 유지하면서 read TPS을 높일 수 있는 방법을 찾아봐야 겠다. 성능을 더 높일 수 있는 방법으로는 mysql의 buffer pool size높이거나 caching 하는 방법이 있을 것이다.

  - hikari 설정
  - maximum-pool-size: 300
  - conection-timeout: 5000
  - max-lifetime: 50000
  - idle-timeout: 50000

- tomcat 설정
  - thread.max: 1000
  - thread.min-spare: 10
  - accept-count: 100 
  - max-connections: 8192 
  - connection-timeout: 60000

- k6 설정
  - stages: [
    {duration: '5s', target: 2000},
    {duration: '10s', target: 0}
  ]

- innodb_buffer_pool_size = 2048mb

![Alt text here](vus2000_read7.png)

결과: innodb_buffer_pool_size를 2GB까지 올리고 TPS가 2000나오는 것을 확인 했다. innodb_buffer_pool_size를 늘리면 read성능이 향상 되는 것을 확인했다.

## STEP2
RDS의 부하를 줄이고 읽기 성능을 높이기 위해 메모리에 데이터를 저장하는 Redis를 도입하였다.

![Alt text here](shorturl2.png)