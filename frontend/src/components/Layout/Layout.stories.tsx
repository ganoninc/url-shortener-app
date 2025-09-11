import type { Meta, StoryObj } from "@storybook/react-vite";
import Layout from "./Layout";
import Title from "../Title/Title";
import Text from "../Text/Text";

const meta: Meta<typeof Layout> = {
  title: "Components/Layout",
  component: Layout,
  tags: ["autodocs"],
  args: {
    header: <Title content="Header"></Title>,
    body: <Text content={"Body"} />,
    footer: <Title level="3" content="Footer"></Title>,
  },
};
export default meta;

type Story = StoryObj<typeof Layout>;

export const Default: Story = {};
