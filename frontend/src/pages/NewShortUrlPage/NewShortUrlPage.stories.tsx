import type { Meta, StoryObj } from "@storybook/react-vite";
import NewShortUrlPage from "./NewShortUrlPage";
import { Provider } from "react-redux";
import { setupStore, type RootState } from "../../redux/store";
import { urlService } from "../../api/client";

const makeStore = (preloadedState?: Partial<RootState>) =>
  setupStore(preloadedState);

const meta: Meta<typeof NewShortUrlPage> = {
  title: "Pages/NewShortUrlPage",
  component: NewShortUrlPage,
  tags: ["autodocs"],
  decorators: [
    (Story, context) => {
      const store = makeStore(context.parameters.preloadedState || {});
      if (context.parameters.mockUrlService) {
        urlService.shortenUrl = context.parameters.mockUrlService;
      }
      return (
        <Provider store={store}>
          <Story />
        </Provider>
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
    },
  },
};

export const WithCorrectUrl: Story = {
  parameters: {
    preloadedState: {
      newShortUrl: { originalUrl: "http://good-url.com" },
    },
    mockUrlService: () => Promise.resolve({ shortId: "Ab4eci" }),
  },
};

export const WhenTheUrlServiceApiIsSlowToAnswer: Story = {
  parameters: {
    preloadedState: {
      newShortUrl: { originalUrl: "http://good-url.com" },
    },
    mockUrlService: () =>
      new Promise((resolve) =>
        setTimeout(() => resolve({ shortId: "8uvicP" }), 5000)
      ),
  },
};

export const WhenTheUrlServiceReturnsAnError: Story = {
  parameters: {
    preloadedState: {
      newShortUrl: { originalUrl: "http://good-url.com" },
    },
    mockUrlService: () => Promise.resolve(new Error("API error")),
  },
};
