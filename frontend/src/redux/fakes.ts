import type { AuthenticatedAuthState } from "./authSlice";

export const fakeAuthenticatedAuthState: AuthenticatedAuthState = {
  status: "authenticated",
  jwt: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNzAwMDAwMDAwfQ.dGVzdC1zaWduYXR1cmUtbm90LXZhbGlk",
  user: { email: "test@test.com" },
};
