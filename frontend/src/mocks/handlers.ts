import { http, HttpResponse } from "msw";
import type { ShortenURLResponseDTO, UserUrlDTO } from "../api/url/generated";
import { fakeShortenURLResponse, fakeUserUrlReponse } from "./fakes";
import { apiGatewayUrl } from "../config/apiGateway";

export const handlers = [
  http.all("*", async () => await delay()),
  http.post(`${apiGatewayUrl}/url/shorten`, () => {
    return HttpResponse.json<ShortenURLResponseDTO>(fakeShortenURLResponse);
  }),
];
