import { request } from "./request";

export const contractAttachmentApi = {
  list: (params: Record<string, unknown>) => request.get("/contract-attachments", { params }),
  create: (data: Record<string, unknown>) => request.post("/contract-attachments", data),
  update: (id: number, data: Record<string, unknown>) => request.put(`/contract-attachments/${id}`, data),
  delete: (id: number) => request.delete(`/contract-attachments/${id}`),
  upload: (contractId: number, file: File) => {
    const formData = new FormData();
    formData.append("contractId", String(contractId));
    formData.append("file", file);
    return request.post("/contract-attachments/upload", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
  },
  preview: (id: number) =>
    request.get<any, Blob>(`/contract-attachments/${id}/download`, {
      params: { inline: true },
      responseType: "blob",
    }),
  download: (id: number) =>
    request.get<any, Blob>(`/contract-attachments/${id}/download`, {
      responseType: "blob",
    }),
};
