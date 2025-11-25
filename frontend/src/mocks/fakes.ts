import type {
  ClickEventDTO,
  ShortUrlClickEventsDTO,
} from "../api/analytics/generated";
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

let fakeClickEventIdIndex = 0;
const COUNTRY_CODE_POOL = ["FR", "US", "DE", "JP"];
const USER_AGENT_POOL = [
  "Mozilla/5.0 (Windows NT 10.0; Win64; x64)",
  "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)",
  "Mozilla/5.0 (iPhone; CPU iPhone OS 16_2 like Mac OS X)",
  "Mozilla/5.0 (Linux; Android 12)",
];

const pick = <T>(arr: T[]) => arr[Math.floor(Math.random() * arr.length)];

const fakeClickEvent = (
  id?: number,
  timestamp?: string,
  userAgent?: string,
  countryCode?: string
): ClickEventDTO => {
  return {
    id: id || fakeClickEventIdIndex++,
    timestamp: timestamp || new Date().toISOString(),
    userAgent: userAgent || pick(USER_AGENT_POOL),
    countryCode: countryCode || pick(COUNTRY_CODE_POOL),
  };
};

export const fakeClickEventsReponse = (
  shortId: string,
  size: number
): ShortUrlClickEventsDTO => {
  const events = Array.from({ length: size }, () => fakeClickEvent());
  const nextCursor = `cursor-${shortId}-${Date.now()}`;

  return {
    totalClicks: 999999,
    events: {
      items: events,
      nextCursor,
    },
  };
};
