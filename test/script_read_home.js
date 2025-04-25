import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  vus: 1000,
  duration: '30s',
  // cloud: {
  //   // Project: Default project
  //   projectID: 3744276,
  //   // Test runs with the same name groups test runs together.
  //   name: 'Test (01/02/2025-20:42:50)'
  // }
};

export default function() {
  const headers = { 'Content-Type': 'application/json' };
  http.get(`http://host.docker.internal:8088/v1/luna-short-url/home`, { headers });
  sleep(1);
}