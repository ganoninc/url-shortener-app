import type { Meta, StoryObj } from "@storybook/react-vite";
import ShortUrlListPage from "./ShortUrlListPage";
import { Provider } from "react-redux";
import { setupStore, type RootState } from "../../redux/store";
import { MemoryRouter, Route, Routes } from "react-router";
import { ROUTES } from "../../routePaths";
import { delay, http, HttpResponse } from "msw";
import { apiGatewayUrl } from "../../config/apiGateway";
import type { UserUrlDTO } from "../../api/url/generated/index";
import { fakeUserUrlListResonse } from "../../mocks/fakes";

const makeStore = (preloadedState?: Partial<RootState>) =>
  setupStore(preloadedState);

const meta: Meta<typeof ShortUrlListPage> = {
  title: "Pages/ShortUrlListPage",
  component: ShortUrlListPage,
  decorators: [
    (Story, context) => {
      const store = makeStore(context.parameters.preloadedState || {});

      return (
        <MemoryRouter initialEntries={[ROUTES.myUrlDetail("abcd")]}>
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

type Story = StoryObj<typeof ShortUrlListPage>;

export const Default: Story = {};

export const SlowLoading: Story = {
  parameters: {
    docs: { disable: true },
    msw: {
      handlers: [
        http.get(`${apiGatewayUrl}/url/my-urls`, async () => {
          await delay(5 * 60 * 1000);
          return HttpResponse.json<UserUrlDTO[]>(fakeUserUrlListResonse);
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
        http.get(`${apiGatewayUrl}/url/my-urls`, () => {
          return HttpResponse.json(null, { status: 500 });
        }),
      ],
    },
  },
};
