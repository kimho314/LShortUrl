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
  const payload = JSON.stringify({
    longUrl: 'https://www.naver.com/cjconnect/service/api/driver/faq/v1/category-meta-list'
  });
  const headers = { 'Content-Type': 'application/json' };
  http.post('http://localhost:8080/api/v1/data/shorten', payload, { headers });
  sleep(1);
}