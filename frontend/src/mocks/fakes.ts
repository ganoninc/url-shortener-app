import type {
  ShortenURLResponseDTO,
  UserUrlDTO,
} from "../api/url/generated/index";

export const fakeShortenURLResponse: ShortenURLResponseDTO = {
  shortId: "abc123",
};

export const fakeUserUrlReponse = (
  shortId: string | undefined = "abcd123",
  originalUrl: string | undefined = "http://www.wikipedia.com/",
  createdAt: string | undefined = "2025-07-18T15:53:22.728466Z"
): UserUrlDTO => {
  return {
    shortId,
    originalUrl,
    createdAt,
  };
};

export const fakeUserUrlListResonse = [
  fakeUserUrlReponse(
    "udiwjo2",
    "https://fr.wikipedia.org/wiki/Rome",
    "2025-07-18T15:53:22.728466Z"
  ),
  fakeUserUrlReponse(
    "bnue92",
    "https://fautvraimentetreconpouravoiruneadresseinternetaussilongue.com",
    "2025-07-16T11:22:31.568463Z"
  ),
  fakeUserUrlReponse(
    "i26iw7",
    "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
    "2025-07-15T09:43:26.963443Z"
  ),
];
