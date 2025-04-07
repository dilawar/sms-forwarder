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
 * SMS type
 */
export type Message = {
  id?: string,
  sender: string;
  message: string;
  body?: string;
  // date and timestamp are the same.
  date?: number;
  timestamp?: number;
  // address and from_address are the same.
  address?: string;
  from_address?: string;
};

