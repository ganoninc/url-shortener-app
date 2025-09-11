import type {} from "vitest/config";
import { defineConfig } from "vitest/config";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  test: {
    environment: "jsdom",
    globals: true,
    setupFiles: ["./jest.setup.ts"],
    projects: [
      {
        test: {
          name: "unit",
          include: ["**/*.test.{ts,tsx}"],
          environment: "jsdom",
          globals: true,
          setupFiles: ["./jest.setup.ts"],
        },
      },
    ],
  },
});
