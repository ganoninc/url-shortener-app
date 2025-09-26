import type {
  ShortenURLResponseDTO,
  UserUrlDTO,
} from "../api/url/generated/index";

export const fakeShortenURLResponse: ShortenURLResponseDTO = {
  shortId: "abc123",
};

export const fakeUserUrlReponse = (
  shortId: string | undefined = "abcd123"
): UserUrlDTO => {
  return {
    shortId,
    originalUrl: "http://www.wikipedia.com/",
    createdAt: "creation date",
  };
};
