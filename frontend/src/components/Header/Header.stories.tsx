import type { Meta, StoryObj } from "@storybook/react-vite";
import Header from "./Header";
import { Provider } from "react-redux";
import { MemoryRouter } from "react-router-dom";
import { setupStore, type RootState } from "../../redux/store";

const makeStore = (preloadedState?: Partial<RootState>) =>
  setupStore(preloadedState);

const meta: Meta<typeof Header> = {
  title: "Components/Header",
  component: Header,
  tags: ["autodocs"],
  decorators: [
    (Story, context) => {
      const store = makeStore(context.parameters.preloadedState || {});
      const routeurInitialEntries = context.parameters
        .routeurInitialEntries || ["/"];

      return (
        <div style={{ minWidth: 800 }}>
          <Provider store={store}>
            <MemoryRouter initialEntries={routeurInitialEntries}>
              <Story />
            </MemoryRouter>
          </Provider>
        </div>
      );
    },
  ],
};
export default meta;

type Story = StoryObj<typeof Header>;

export const UserLoggedOut: Story = {};

export const UserLoggedIn: Story = {
  parameters: {
    preloadedState: {
      auth: { accessToken: "access-token", email: "test@test.com" },
    },
  },
};
