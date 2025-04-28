import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  stages: [
    {duration: '5s', target: 2000},
    {duration: '10s', target: 0}
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
  // const urls = ['eqP5FsNmiHHcGqSTgquNuWr5UX8b'];
  const headers = { 'Content-Type': 'application/json' };
  // let shortUrl = urls[iter % urls.length];
  const shortUrl = '1780Gh5aTBWmGOMp8oVHtqrN00pwz';
  // http.get(`http://host.docker.internal:8080/api/v1/${shortUrl}`, { headers });
  http.get(`http://host.docker.internal:80/api/v1/${shortUrl}`, { headers, redirects: 0 });
  sleep(1);
}