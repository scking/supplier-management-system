import { request } from "./request";

export const contractApi = {
  list: (params: Record<string, unknown>) => request.get("/contracts", { params }),
  detail: (id: number) => request.get(`/contracts/${id}`),
  reminders: () => request.get("/contracts/reminders"),
  create: (data: Record<string, unknown>) => request.post("/contracts", data),
  update: (id: number, data: Record<string, unknown>) => request.put(`/contracts/${id}`, data),
  delete: (id: number) => request.delete(`/contracts/${id}`),
};
