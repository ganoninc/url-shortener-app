import type { Meta, StoryObj } from "@storybook/react-vite";
import LoginPage from "./LoginPage";
import { Provider } from "react-redux";
import { setupStore, type RootState } from "../../redux/store";

const makeStore = (preloadedState?: Partial<RootState>) =>
  setupStore(preloadedState);

const meta: Meta<typeof LoginPage> = {
  title: "Pages/LoginPage",
  component: LoginPage,
  tags: ["autodocs"],
  decorators: [
    (Story, context) => {
      const store = makeStore(context.parameters.preloadedState || {});

      return (
        <>
          <Provider store={store}>
            <Story />
          </Provider>
        </>
      );
    },
  ],
};
export default meta;

type Story = StoryObj<typeof LoginPage>;

export const Default: Story = {};
