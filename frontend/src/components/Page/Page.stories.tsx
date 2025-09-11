import type { Meta, StoryObj } from "@storybook/react-vite";
import Page from "./Page";
import Title from "../Title/Title";
import Text from "../Text/Text";

const meta: Meta<typeof Page> = {
  title: "Components/Page",
  component: Page,
  tags: ["autodocs"],
  args: {
    children: (
      <>
        <Title content="This is a Page" />
        <Text content="With some sample content passed as children." />
      </>
    ),
  },
};
export default meta;

type Story = StoryObj<typeof Page>;

export const Default: Story = {};
