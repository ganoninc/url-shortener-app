import { useCallback, useEffect, useRef, useState } from "react";
import { analyticsService } from "../api/client";
import type { ClickEventDTO } from "../api/analytics/generated";

type UseClickEventsState =
  | {
      status: "initial" | "loading" | "loaded";
      totalClicks?: number;
      clickEvents: ClickEventDTO[];
      nextCursor?: string;
    }
  | {
      status: "error";
      totalClicks?: number;
      clickEvents: ClickEventDTO[];
      errorMessage: string;
    };

export function useClickEvents(shortId: string) {
  const [state, setState] = useState<UseClickEventsState>({
    status: "initial",
    clickEvents: [],
  });

  const abortControllerRef = useRef<AbortController | null>(null);
  const pageSize = 25;

  const loadEvents = useCallback(
    (nextCursor?: string) => {
      if (abortControllerRef.current) {
        abortControllerRef.current.abort();
      }

      setState((prev) => ({ ...prev, status: "loading" }));

      const controller = new AbortController();
      abortControllerRef.current = controller;

      analyticsService
        .getClickEvents(shortId, nextCursor, pageSize, {
          signal: controller.signal,
        })
        .then((res) => {
          setState((prev) => {
            return {
              status: "loaded",
              totalClicks: res.data.totalClicks,
              clickEvents:
                prev.status === "initial"
                  ? res.data.events.items
                  : [...prev.clickEvents, ...res.data.events.items],
              nextCursor: res.data.events.nextCursor,
            };
          });
        })
        .catch((reason) => {
          if (reason.name === "AbortError") return;

          setState((prev) => {
            return {
              ...prev,
              status: "error",
              errorMessage: reason.message,
            };
          });
        });
    },
    [shortId]
  );

  useEffect(() => {
    setState({
      status: "loading",
      clickEvents: [],
    });
    loadEvents();

    return () => {
      if (abortControllerRef.current) {
        abortControllerRef.current.abort();
      }
    };
  }, [loadEvents]);

  const hasMore = state.status === "loaded" && !!state.nextCursor;
  const isLoading = state.status === "loading";
  const errorMessage =
    state.status === "error" ? state.errorMessage : undefined;

  const loadMore = useCallback(() => {
    if (state.status === "loaded" && state.nextCursor) {
      loadEvents(state.nextCursor);
    }
  }, [state, loadEvents]);

  return {
    clickEvents: state.clickEvents,
    totalClicks: state.totalClicks,
    isLoading,
    hasMore,
    errorMessage,
    loadMore,
  };
}
