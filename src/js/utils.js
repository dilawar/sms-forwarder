//! Helper methods.

import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
dayjs.extend(relativeTime)

export const fromNow = (datetime) => {
  return dayjs(datetime).fromNow();
}

