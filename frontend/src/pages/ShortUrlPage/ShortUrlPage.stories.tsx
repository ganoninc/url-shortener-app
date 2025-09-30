import type { Meta, StoryObj } from "@storybook/react-vite";
import ShortUrlPage from "./ShortUrlPage";
import { Provider } from "react-redux";
import { setupStore, type RootState } from "../../redux/store";
import { MemoryRouter, Route, Routes } from "react-router";
import { ROUTES } from "../../routePaths";
import { delay, http, HttpResponse } from "msw";
import { apiGatewayUrl } from "../../config/apiGateway";
import type { UserUrlDTO } from "../../api/url/generated/index";
import { fakeUserUrlReponse } from "../../mocks/fakes";

const makeStore = (preloadedState?: Partial<RootState>) =>
  setupStore(preloadedState);

const meta: Meta<typeof ShortUrlPage> = {
  title: "Pages/ShortUrlPage",
  component: ShortUrlPage,
  decorators: [
    (Story, context) => {
      const store = makeStore(context.parameters.preloadedState || {});

      return (
        <MemoryRouter initialEntries={[ROUTES.userShortUrlDetail("abcd")]}>
          <Provider store={store}>
            <Routes>
              <Route path="/my-urls/:shortId" element={<Story />} />
            </Routes>
          </Provider>
        </MemoryRouter>
      );
    },
  ],
};
export default meta;

type Story = StoryObj<typeof ShortUrlPage>;

export const Default: Story = {};

export const NewlyCreated: Story = {
  args: {
    newShortUrl: true,
  },
};

export const SlowLoading: Story = {
  parameters: {
    docs: { disable: true },
    msw: {
      handlers: [
        http.get(`${apiGatewayUrl}/url/my-urls/abcd`, async () => {
          await delay(5 * 60 * 1000);
          return HttpResponse.json<UserUrlDTO>(fakeUserUrlReponse("22222"));
        }),
      ],
    },
  },
};

export const WhenTheUrlServiceReturnsAnError: Story = {
  parameters: {
    docs: { disable: true },
    msw: {
      handlers: [
        http.get(`${apiGatewayUrl}/url/my-urls/abcd`, () => {
          return HttpResponse.json(null, { status: 500 });
        }),
      ],
    },
  },
};
