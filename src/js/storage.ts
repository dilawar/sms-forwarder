/* Storage API. */

import { Preferences } from '@capacitor/preferences'
import { type Rule } from './types'

export const storeRule = async (key: string, rule: Rule) => {
    await Preferences.set({key: key, value: JSON.stringify(rule)})
}

export const loadRule = async (key: string): Promise<Rule | null> => {
    const { value } =  await Preferences.get({ key: key })
    if( ! value) {
        return null
    }
    return JSON.parse(value)
}
