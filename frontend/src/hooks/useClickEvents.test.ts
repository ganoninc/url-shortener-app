import { act, renderHook, waitFor } from "@testing-library/react";
import { server } from "../mocks/node";
import { useClickEvents } from "./useClickEvents";
import { http, HttpResponse } from "msw";
import { apiGatewayUrl } from "../config/apiGateway";
import { fakeClickEvent } from "../mocks/fakes";
import type {
  ClickEventDTO,
  ShortUrlClickEventsDTO,
} from "../api/analytics/generated";

function mockResponse({
  items,
  nextCursor = undefined,
  totalClicks = 100,
}: {
  items: ClickEventDTO[];
  nextCursor?: string | undefined;
  totalClicks?: number;
}) {
  return {
    totalClicks,
    events: { items, nextCursor },
  };
}

describe("useClickEvents", () => {
  const shortId = "abc123";

  beforeEach(() => {
    vi.restoreAllMocks();
  });

  it("loads initial events", async () => {
    const fakeClickEvent1 = fakeClickEvent(1);
    const mockedResponse = mockResponse({
      items: [fakeClickEvent1],
      nextCursor: undefined,
      totalClicks: 1,
    });

    server.use(
      http.get(
        `${apiGatewayUrl}/analytics/short-url/${shortId}`,
        () => {
          return HttpResponse.json<ShortUrlClickEventsDTO>(mockedResponse);
        },
        { once: true }
      )
    );

    const { result } = renderHook(() => useClickEvents(shortId));

    expect(result.current.isLoading).toBe(true);

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
    });

    expect(result.current.clickEvents.length).toBe(1);
    expect(result.current.hasMore).toBe(false);
    expect(result.current.totalClicks).toBe(1);
  });

  it("loads more events when calling loadMore", async () => {
    const fakeClickEvent1 = fakeClickEvent(1);
    const fakeClickEvent2 = fakeClickEvent(2);
    const fakeClickEvent3 = fakeClickEvent(3);

    const mockedResponse1 = mockResponse({
      items: [fakeClickEvent1, fakeClickEvent2],
      nextCursor: "nextCursor",
      totalClicks: 3,
    });

    const mockedResponse2 = mockResponse({
      items: [fakeClickEvent3],
      nextCursor: undefined,
      totalClicks: 3,
    });

    server.use(
      http.get(
        `${apiGatewayUrl}/analytics/short-url/${shortId}`,
        () => {
          return HttpResponse.json<ShortUrlClickEventsDTO>(mockedResponse1);
        },
        { once: true }
      )
    );

    const { result } = renderHook(() => useClickEvents(shortId));

    await waitFor(() => expect(result.current.isLoading).toBe(false));

    expect(result.current.clickEvents.map((e) => e.id)).toEqual([
      fakeClickEvent1.id,
      fakeClickEvent2.id,
    ]);
    expect(result.current.hasMore).toBe(true);

    server.use(
      http.get(
        `${apiGatewayUrl}/analytics/short-url/${shortId}`,
        () => {
          return HttpResponse.json<ShortUrlClickEventsDTO>(mockedResponse2);
        },
        { once: true }
      )
    );

    await act(async () => {
      result.current.loadMore();
    });

    await waitFor(() => {
      expect(result.current.hasMore).toBe(false);
    });

    expect(result.current.clickEvents.map((e) => e.id)).toEqual([
      fakeClickEvent1.id,
      fakeClickEvent2.id,
      fakeClickEvent3.id,
    ]);
  });

  it("handles errors", async () => {
    server.use(
      http.get(`${apiGatewayUrl}/analytics/short-url/${shortId}`, () => {
        return HttpResponse.error();
      })
    );

    const { result } = renderHook(() => useClickEvents(shortId));

    await waitFor(() => {
      expect(result.current.errorMessage).toBeDefined();
    });

    expect(result.current.isLoading).toBe(false);
    expect(result.current.clickEvents).toEqual([]);
  });
});
