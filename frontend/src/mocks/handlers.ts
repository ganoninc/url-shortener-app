import { http, HttpResponse } from "msw";
import type { ShortenURLResponseDTO } from "../api/url/generated/index";
import { fakeShortenURLResponse } from "./fakes";

const API_GATEWAY_URL = import.meta.env.VITE_API_GATEWAY_URL;

export const handlers = [
  http.post("/" + API_GATEWAY_URL + "/url/shorten", () => {
    return HttpResponse.json<ShortenURLResponseDTO>(fakeShortenURLResponse);
  }),
];
