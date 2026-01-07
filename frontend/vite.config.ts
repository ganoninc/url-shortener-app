/// <reference types="vitest/config" />
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  base: "/dashboard",
  server: {
    headers: {
      "Service-Worker-Allowed": "/",
    },
  },
  css: {
    modules: {
      localsConvention: "camelCaseOnly",
    },
    devSourcemap: true,
  },
  test: {
    environment: "jsdom",
    globals: true,
    setupFiles: ["./vitest.setup.ts"],
    include: ["**/*.test.{ts,tsx}"],
  },
});
