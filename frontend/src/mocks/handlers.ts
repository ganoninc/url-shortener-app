import { delay, http, HttpResponse } from "msw";
import type { ShortenURLResponseDTO, UserUrlDTO } from "../api/url/generated";
import type { ShortUrlClickEventsDTO } from "../api/analytics/generated";
import {
  fakeClickEventsReponse,
  fakeShortenURLResponse,
  fakeUserUrlListResonse,
  fakeUserUrlReponse,
} from "./fakes";
import { apiGatewayUrl } from "../config/apiGateway";

export const handlers = [
  http.all("*", async () => await delay()),
  // url-services
  http.post(`${apiGatewayUrl}/url/shorten`, () => {
    return HttpResponse.json<ShortenURLResponseDTO>(fakeShortenURLResponse);
  }),
  http.get(`${apiGatewayUrl}/url/my-urls`, () => {
    return HttpResponse.json<UserUrlDTO[]>(fakeUserUrlListResonse);
  }),
  http.get(`${apiGatewayUrl}/url/my-urls/:shortId`, (req) => {
    const shortId = req.params.shortId as string;

    return HttpResponse.json<UserUrlDTO>(fakeUserUrlReponse(shortId));
  }),
  // analytics-service
  http.get(
    `${apiGatewayUrl}/analytics/short-url/:shortId`,
    ({ params, request }) => {
      const shortId = params.shortId as string;

      const url = new URL(request.url);
      const size = Number.parseInt(url.searchParams.get("size") || "50");

      return HttpResponse.json<ShortUrlClickEventsDTO>(
        fakeClickEventsReponse(shortId, size)
      );
    }
  ),
  // auth-service
  http.get(`${apiGatewayUrl}/auth/refresh-access-token`, () => {
    return HttpResponse.text("new-access-token");
  }),
];
