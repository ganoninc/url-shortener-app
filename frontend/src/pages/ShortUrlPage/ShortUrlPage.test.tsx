import { waitFor, screen } from "@testing-library/react";
import ShortUrlPage from "./ShortUrlPage";
import { server } from "../../mocks/node";
import { apiGatewayUrl } from "../../config/apiGateway";
import { fakeUserUrlReponse } from "../../mocks/fakes";
import { renderWithProviders } from "../../utils/test-utils";
import { http, HttpResponse } from "msw";
import { vi } from "vitest";

const shortUrlData = fakeUserUrlReponse();

const mockedUseNavigate = vi.fn();
vi.mock("react-router-dom", async () => {
  const actual = vi.importActual("react-router-dom");
  return {
    ...actual,
    useNavigate: () => mockedUseNavigate,
    useParams: vi.fn(() => ({
      shortId: shortUrlData.shortId,
    })),
  };
});

describe("ShortUrlPage", () => {
  beforeAll(() => {
    server.listen();
  });

  afterEach(() => {
    server.resetHandlers();
  });

  afterAll(() => server.close());

  it("displays a loading img when loading short URL data", () => {
    renderWithProviders(<ShortUrlPage />);

    const loadingImg = screen.getByAltText("Loading...");
    expect(loadingImg).toBeInTheDocument();
  });

  it("loads and displays short URL data", async () => {
    renderWithProviders(<ShortUrlPage />);

    await waitFor(() => {
      const shortUrlCopyableInput = screen.getByDisplayValue(
        `${apiGatewayUrl}/${fakeUserUrlReponse().shortId}`
      );
      expect(shortUrlCopyableInput).toBeInTheDocument();

      const qrCodeImg = screen.getByRole("img");
      expect(qrCodeImg).toBeInTheDocument();
    });
  });

  it("displays an error when urlService is returning an error", async () => {
    server.use(
      http.get(`${apiGatewayUrl}/my-urls/${shortUrlData.shortId}}`, () => {
        return HttpResponse.json({ error: "Error Message." }, { status: 500 });
      })
    );

    renderWithProviders(<ShortUrlPage />);

    await waitFor(() => {
      const errorMessage = screen.getByRole("alert");
      expect(errorMessage).toBeInTheDocument();
    });
  });
});
