import { v7 as uuidv7 } from 'uuid';

/**
 * Types
 */

/**
 * Rule
 */
export type Rule = {
  // must be unique.
  id: string;
  // glob that matches a given message's body.
  glob: string;
  // Address of the sender or senders
  sender: string;
  // List of addresses this message should be forward to
  forward: string;
};

/**
 * Factory function to create a default Rule
 */
export const createRule = (glob = '', sender = '', forward = ''): Rule => {
  return { id: uuidv7(), glob: glob, sender: sender, forward: forward };
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

export type Message = {
  readonly id: string;

  // body of the message
  readonly body: string;
  // date and timestamp are the same.
  readonly timestamp: number;
  // address and from_address are the same.
  readonly sender: string;

  // matched rule
  matched_rules: Rule[];
  // forwarded
  is_forwarded?: boolean;
};
