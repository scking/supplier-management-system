import { request } from "./request";

export const productAttachmentApi = {
  list: (params: Record<string, unknown>) => request.get("/product-attachments", { params }),
  create: (data: Record<string, unknown>) => request.post("/product-attachments", data),
  update: (id: number, data: Record<string, unknown>) => request.put(`/product-attachments/${id}`, data),
  delete: (id: number) => request.delete(`/product-attachments/${id}`),
};
