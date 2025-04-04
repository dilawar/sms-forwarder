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
