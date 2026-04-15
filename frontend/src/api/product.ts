import { request } from "./request";

export const productApi = {
  list: (params: Record<string, unknown>) => request.get("/products", { params }),
  create: (data: Record<string, unknown>) => request.post("/products", data),
  update: (id: number, data: Record<string, unknown>) => request.put(`/products/${id}`, data),
  delete: (id: number) => request.delete(`/products/${id}`),
  paramList: (params: Record<string, unknown>) => request.get("/product-params", { params }),
  paramCreate: (data: Record<string, unknown>) => request.post("/product-params", data),
  paramUpdate: (id: number, data: Record<string, unknown>) => request.put(`/product-params/${id}`, data),
  paramDelete: (id: number) => request.delete(`/product-params/${id}`),
  attachmentList: (params: Record<string, unknown>) => request.get("/product-attachments", { params }),
  attachmentCreate: (data: Record<string, unknown>) => request.post("/product-attachments", data),
  attachmentUpdate: (id: number, data: Record<string, unknown>) => request.put(`/product-attachments/${id}`, data),
  attachmentDelete: (id: number) => request.delete(`/product-attachments/${id}`),
};
