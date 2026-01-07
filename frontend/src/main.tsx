import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { Provider } from "react-redux";
import "./index.css";
import App from "./App.tsx";
import { BrowserRouter } from "react-router-dom";
import { ErrorBoundary } from "react-error-boundary";
import ErrorBoudaryFallbackView from "./components/ErrorBoudaryFallbackView/ErrorBoudaryFallbackView.tsx";
import { store } from "./redux/store.ts";
import { isMSWEnabled } from "./config/msw.ts";

async function enableMocking() {
  if (!isMSWEnabled || typeof window === "undefined") {
    return;
  }

  const { worker } = await import("./mocks/browser.ts");
  return worker.start({
    serviceWorker: {
      url: `${import.meta.env.BASE_URL}/mockServiceWorker.js`,
      options: {
        scope: "/",
      },
    },
  });
}

enableMocking().then(() => {
  createRoot(document.getElementById("root")!).render(
    <StrictMode>
      <ErrorBoundary FallbackComponent={ErrorBoudaryFallbackView}>
        <BrowserRouter basename={`${import.meta.env.BASE_URL}`}>
          <Provider store={store}>
            <App />
          </Provider>
        </BrowserRouter>
      </ErrorBoundary>
    </StrictMode>
  );
});
