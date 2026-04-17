import axios from "axios";

// 调用项目管理系统的只读接口，获取项目列表用于供应商合同关联
function resolveProjectBaseUrl() {
  if (typeof window === "undefined") return "http://127.0.0.1:9280/api";
  const { hostname } = window.location;
  if (hostname === "127.0.0.1" || hostname === "localhost") {
    // dev: 由 vite proxy /pm-api → http://127.0.0.1:9280/api
    return "/pm-api";
  }
  // prod: nginx proxy /pm-api → http://host.docker.internal:9280/api
  return "/pm-api";
}

const pmRequest = axios.create({
  baseURL: resolveProjectBaseUrl(),
  timeout: 10000,
  withCredentials: false,
});

pmRequest.interceptors.request.use((config) => {
  const token = localStorage.getItem("supplier_access_token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

pmRequest.interceptors.response.use((r) => r.data, (e) => Promise.reject(e));

export const projectExternalApi = {
  projectList: () => pmRequest.get<any, any>("/pm/projects"),
};
