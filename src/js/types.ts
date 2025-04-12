/**
 * Types
 */

/**
 * Rule
 */
export type Rule = {
  // glob that matches a given message's body.
  glob: string;

  // Address of the sender or senders
  sender?: string;
  senders: Array<string>;

  // List of addresses this message should be forward to
  forward?: string;
  forwards: Array<string>;
};

/**
 * Factory function to create a default Rule
 */
export const createRule = (glob = '', senders = [], forwards = []): Rule => {
  return { glob: glob, senders: senders, forwards: forwards };
};

/**
 * What kind of rule match using BitFlags.
 *
 * - 'None' means neither sender or body match
 * - 'Sender' means sender match
 * - 'Body' means body match
 * - 'Both' means both 'sender' and 'body' match
 */
export enum RuleMatchType {
  None = 0,
  Sender = 1 << 0, // 0001
  Body = 1 << 1, // 0010
  Both = 3, // 0011
}

export interface Message {
  readonly id: string;
  readonly sender: string;
  readonly message: string;
  // body of the message
  readonly body?: string;

  // date and timestamp are the same.
  readonly date?: number;
  readonly timestamp?: number;

  // address and from_address are the same.
  readonly address?: string;
  readonly from_address?: string;

  // matched rule
  matched_rules: Rule[];
  // forwarded
  is_forwarded?: boolean;
}
