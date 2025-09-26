import { delay, http, HttpResponse } from "msw";
import type { ShortenURLResponseDTO, UserUrlDTO } from "../api/url/generated";
import { fakeShortenURLResponse, fakeUserUrlReponse } from "./fakes";
import { apiGatewayUrl } from "../config/apiGateway";

export const handlers = [
  http.all("*", async () => await delay()),
  http.post(`${apiGatewayUrl}/url/shorten`, () => {
    return HttpResponse.json<ShortenURLResponseDTO>(fakeShortenURLResponse);
  }),
  http.get(`${apiGatewayUrl}/url/my-urls/:shortId`, (req) => {
    const shortId = req.params.shortId as string;

    return HttpResponse.json<UserUrlDTO>(fakeUserUrlReponse(shortId));
  }),
];
