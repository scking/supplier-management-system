import { request } from "./request";

export const evaluationApi = {
  list: (params: Record<string, unknown>) => request.get("/evaluations", { params }),
  create: (data: Record<string, unknown>) => request.post("/evaluations", data),
  update: (id: number, data: Record<string, unknown>) => request.put(`/evaluations/${id}`, data),
  delete: (id: number) => request.delete(`/evaluations/${id}`),
};
