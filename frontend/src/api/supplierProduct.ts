import { request } from "./request";

export const supplierProductApi = {
  list: (params: Record<string, unknown>) => request.get("/supplier-products", { params }),
  create: (data: Record<string, unknown>) => request.post("/supplier-products", data),
  update: (id: number, data: Record<string, unknown>) => request.put(`/supplier-products/${id}`, data),
  delete: (id: number) => request.delete(`/supplier-products/${id}`),
  priceHistoryList: (params: Record<string, unknown>) => request.get("/supplier-product-price-histories", { params }),
  priceHistoryCreate: (data: Record<string, unknown>) => request.post("/supplier-product-price-histories", data),
  priceHistoryUpdate: (id: number, data: Record<string, unknown>) => request.put(`/supplier-product-price-histories/${id}`, data),
  priceHistoryDelete: (id: number) => request.delete(`/supplier-product-price-histories/${id}`),
};
