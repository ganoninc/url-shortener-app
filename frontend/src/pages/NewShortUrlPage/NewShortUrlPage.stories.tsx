import type { Meta, StoryObj } from "@storybook/react-vite";
import NewShortUrlPage from "./NewShortUrlPage";
import { Provider } from "react-redux";
import { setupStore, type RootState } from "../../redux/store";
import { MemoryRouter } from "react-router-dom";
import { fakeAuthenticatedAuthState } from "../../redux/fakes";
import { apiGatewayUrl } from "../../config/apiGateway";
import { delay, http, HttpResponse } from "msw";
import type { ShortenURLResponseDTO } from "../../api/url/generated";
import { fakeShortenURLResponse } from "../../mocks/fakes";

const makeStore = (preloadedState?: Partial<RootState>) =>
  setupStore(preloadedState);

const meta: Meta<typeof NewShortUrlPage> = {
  title: "Pages/NewShortUrlPage",
  component: NewShortUrlPage,
  tags: ["autodocs"],
  decorators: [
    (Story, context) => {
      const store = makeStore(context.parameters.preloadedState || {});

      return (
        <MemoryRouter>
          <Provider store={store}>
            <Story />
          </Provider>
        </MemoryRouter>
      );
    },
  ],
};
export default meta;

type Story = StoryObj<typeof NewShortUrlPage>;

export const Empty: Story = {};

export const WithWrongUrl: Story = {
  parameters: {
    preloadedState: {
      newShortUrl: { originalUrl: "wrong-url" },
      auth: fakeAuthenticatedAuthState,
    } as RootState,
  },
};

export const WithCorrectUrl: Story = {
  parameters: {
    preloadedState: {
      newShortUrl: { originalUrl: "http://good-url.com" },
      auth: fakeAuthenticatedAuthState,
    } as RootState,
  },
};

export const WhenTheUrlServiceApiIsSlowToAnswer: Story = {
  parameters: {
    preloadedState: {
      newShortUrl: { originalUrl: "http://good-url.com" },
      auth: fakeAuthenticatedAuthState,
    } as RootState,
    msw: {
      handlers: [
        http.post(`${apiGatewayUrl}/url/shorten`, async () => {
          await delay(5000);

          return HttpResponse.json<ShortenURLResponseDTO>(
            fakeShortenURLResponse
          );
        }),
      ],
    },
  },
};

export const WhenTheUrlServiceReturnsAnError: Story = {
  parameters: {
    preloadedState: {
      newShortUrl: { originalUrl: "http://good-url.com" },
      auth: fakeAuthenticatedAuthState,
    } as RootState,
    msw: {
      handlers: [
        http.post(`${apiGatewayUrl}/url/shorten`, () => {
          return HttpResponse.json({}, { status: 500 });
        }),
      ],
    },
  },
};
