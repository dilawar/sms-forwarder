import { registerPlugin } from '@capacitor/core';
import { type Message } from '../js/types';

export interface SmsPlugin {
  // For testing.
  echo(options: { value: string }): Promise<{ value: string }>;

  // Read live sms.
  getLiveSms(): Promise<{ result: Message[] }>;

  // Query SMS
  querySms(options: { query: string }): Promise<{ result: Message[] }>;

  // Request permission.
  requestPermissions(): Promise<void>;
}

const Sms = registerPlugin<SmsPlugin>('SmsPlugin');

export default Sms;
