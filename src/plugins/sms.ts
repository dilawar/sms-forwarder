import { registerPlugin } from '@capacitor/core';

export type Message = {
  sender: string;
  message: string;
};

export interface SmsPlugin {
  // For testing.
  echo(options: { value: string }): Promise<{ value: string }>;
  // Read live sms.
  getLiveSms(): Promise<{ result: Message[] }>;
  requestPermissions(): Promise<void>;
}

const Sms = registerPlugin<SmsPlugin>('SmsPlugin');

export default Sms;
