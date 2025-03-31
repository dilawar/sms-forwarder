import { registerPlugin } from '@capacitor/core';

export interface SmsPlugin {
    echo(options: { value: string }): Promise<{value: string}>;
}

const Sms = registerPlugin<SmsPlugin>('SmsPlugin');

export default Sms
