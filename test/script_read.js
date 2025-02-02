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
  const shortUrl = 'd5OWfW6LKAKRVTAxiembV1P02BVG';
  const headers = { 'Content-Type': 'application/json' };
  http.get(`http://localhost:8080/api/v1/${shortUrl}`, { headers });
  sleep(1);
}