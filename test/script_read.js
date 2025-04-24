import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  stages: [
    {duration: '10s', target: 3000},
    {duration: '30s', target: 0}
  ]
  // vus: 100,
  // duration: '30s',
  // cloud: {
  //   // Project: Default project
  //   projectID: 3744276,
  //   // Test runs with the same name groups test runs together.
  //   name: 'Test (01/02/2025-20:42:50)'
  // }
};

export default function() {
  let iter = __ITER;
  const urls = ['pUj7pma2Ta3fiXSU3MGNY5ybDsSX'];
  const headers = { 'Content-Type': 'application/json' };
  let shortUrl = urls[iter % urls.length];
  // http.get(`http://host.docker.internal:8080/api/v1/${shortUrl}`, { headers });
  http.get(`http://host.docker.internal:8888/api/v1/${shortUrl}`, { headers });
  sleep(1);
}