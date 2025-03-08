import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  vus: 100,
  duration: '30s',
  cloud: {
    // Project: Default project
    projectID: 3744276,
    // Test runs with the same name groups test runs together.
    name: 'Test (01/02/2025-20:42:50)'
  }
};

export default function() {
  let vuId = __VU;
  let iter = __ITER;
  let id = `vu-${vuId}-iter-${iter}`;
  const payload = JSON.stringify({
    longUrl: `http://localhost:8080/v1/luna-short-url/greeting?name=${id}`
  });
  // const payload = JSON.stringify({
  //   longUrl: `http://localhost:8080/v1/luna-short-url/greeting?name=vu-90-iter-4`
  // });
  const headers = { 'Content-Type': 'application/json' };
  http.post('http://localhost:8080/api/v1/data/shorten', payload, { headers });
  sleep(1);
}