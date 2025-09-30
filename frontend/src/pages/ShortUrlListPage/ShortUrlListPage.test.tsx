import { waitFor, screen } from "@testing-library/react";
import ShortUrlListPage from "./ShortUrlListPage";
import { server } from "../../mocks/node";
import { apiGatewayUrl } from "../../config/apiGateway";
import { fakeUserUrlListResonse, fakeUserUrlReponse } from "../../mocks/fakes";
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
  };
});

describe("ShortUrlListPage", () => {
  beforeAll(() => {
    server.listen();
  });

  afterEach(() => {
    server.resetHandlers();
  });

  afterAll(() => server.close());

  it("displays a loading img when loading short URL data", () => {
    renderWithProviders(<ShortUrlListPage />);

    const loadingImg = screen.getByAltText("Loading...");
    expect(loadingImg).toBeInTheDocument();
  });

  it("loads and displays short URL list", async () => {
    renderWithProviders(<ShortUrlListPage />);

    await waitFor(() => {
      expect(
        screen.getByText(fakeUserUrlListResonse[0].originalUrl)
      ).toBeInTheDocument();
      expect(
        screen.getByText(fakeUserUrlListResonse[1].originalUrl)
      ).toBeInTheDocument();
      expect(
        screen.getByText(fakeUserUrlListResonse[2].originalUrl)
      ).toBeInTheDocument();
    });
  });

  it("displays an error when urlService is returning an error", async () => {
    server.use(
      http.get(`${apiGatewayUrl}/my-urls/${shortUrlData.shortId}}`, () => {
        return HttpResponse.json({ error: "Error Message." }, { status: 500 });
      })
    );

    renderWithProviders(<ShortUrlListPage />);

    await waitFor(() => {
      const errorMessage = screen.getByRole("alert");
      expect(errorMessage).toBeInTheDocument();
    });
  });
});
