import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { Provider } from "react-redux";
import { setupStore } from "./redux/store.ts";
import "./index.css";
import App from "./App.tsx";
import { BrowserRouter } from "react-router-dom";
import { createClient } from "./api/client.ts";
import { ErrorBoundary } from "react-error-boundary";
import { ErrorBoudaryFallbackView } from "./components/ErrorBoudaryFallbackView/ErrorBoudaryFallbackView.tsx";

const store = setupStore();
export const client = createClient(store);

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ErrorBoundary FallbackComponent={ErrorBoudaryFallbackView}>
      <BrowserRouter>
        <Provider store={store}>
          <App />
        </Provider>
      </BrowserRouter>
    </ErrorBoundary>
  </StrictMode>
);
