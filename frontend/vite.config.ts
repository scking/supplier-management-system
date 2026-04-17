import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import { fileURLToPath, URL } from "node:url";

export default defineConfig({
  base: process.env.NODE_ENV === "production" ? "/supplier/" : "/",
  plugins: [vue()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  server: {
    host: "127.0.0.1",
    port: 3180,
    proxy: {
      "/pm-api": {
        target: "http://127.0.0.1:9280",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/pm-api/, "/api"),
      },
    },
  },
});
