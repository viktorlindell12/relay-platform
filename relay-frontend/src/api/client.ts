import axios from 'axios';

const client = axios.create({ baseURL: '/api' });

client.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

let redirectingToLogin = false;

client.interceptors.response.use(
  r => r,
  err => {
    if (
      !redirectingToLogin &&
      axios.isAxiosError(err) &&
      err.response?.status === 401 &&
      !err.config?.url?.startsWith('/auth/')
    ) {
      redirectingToLogin = true;
      localStorage.removeItem('token');
      localStorage.removeItem('userId');
      window.location.href = '/login';
    }
    return Promise.reject(err);
  }
);

export default client;