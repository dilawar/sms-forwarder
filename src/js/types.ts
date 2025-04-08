/**
 * Types
 */

/**
 * Rule
 */
export type Rule = {
  glob: string;
  address: string;
};

/**
 * Factory function to create a default Rule
 */
export const createRule = (glob: string = '', address: string = ''): Rule => {
  return { glob: glob, address: address };
};

/**
 * What kind of rule match?
 *
 * - 'sender' means sender match
 * - 'body' means body match
 * - 'none' means neither sender or body match
 * - 'both' means both 'sender' and 'body' match
 */
export enum RuleMatchType {
  Sender,
  Body,
  None,
  Both,
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


