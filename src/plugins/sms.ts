import { registerPlugin } from '@capacitor/core';
import { type Message } from '../js/types';

export interface SmsPlugin {
  // For testing.
  echo(options: { value: string }): Promise<{ value: string }>;

  // Read live sms.
  getLiveSms(): Promise<{ result: Message[] }>;

  // Query SMS
  querySms(options: { query: string }): Promise<{ result: Message[] }>;

  // Send SMS
  sendMessage(options: { forward: string; body: string }): Promise<{ result: boolean }>;

  // Request permission.
  requestPermissions(): Promise<void>;
}

const Sms = registerPlugin<SmsPlugin>('SmsPlugin');

export default Sms;
