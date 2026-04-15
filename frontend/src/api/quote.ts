import { request } from "./request";

export const quoteApi = {
  list: (params: Record<string, unknown>) => request.get("/quotes", { params }),
  itemList: (params: Record<string, unknown>) => request.get("/quote-items", { params }),
  create: (data: Record<string, unknown>) => request.post("/quotes", data),
  update: (id: number, data: Record<string, unknown>) => request.put(`/quotes/${id}`, data),
  delete: (id: number) => request.delete(`/quotes/${id}`),
  recommend: (inquiryId: number) => request.post(`/quotes/recommend/${inquiryId}`),
};
