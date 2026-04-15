import { request } from "./request";

export const inquiryApi = {
  list: (params: Record<string, unknown>) => request.get("/inquiries", { params }),
  create: (data: Record<string, unknown>) => request.post("/inquiries", data),
  update: (id: number, data: Record<string, unknown>) => request.put(`/inquiries/${id}`, data),
  delete: (id: number) => request.delete(`/inquiries/${id}`),
  compare: (id: number) => request.post(`/inquiries/${id}/compare`),
};
