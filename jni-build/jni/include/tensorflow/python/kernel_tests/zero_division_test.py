# Copyright 2015 The TensorFlow Authors. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ==============================================================================

"""Tests for integer division by zero."""

from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import tensorflow as tf


class ZeroDivisionTest(tf.test.TestCase):

  def testZeros(self):
    with self.test_session():
      for dtype in tf.uint8, tf.int16, tf.int32, tf.int64:
        zero = tf.constant(0, dtype=dtype)
        one = tf.constant(1, dtype=dtype)
        bads = [one // zero]
        if dtype in (tf.int32, tf.int64):
          bads.append(one % zero)
        for bad in bads:
          try:
            result = bad.eval()
          except tf.OpError as e:
            # Ideally, we'd get a nice exception.  In theory, this should only
            # happen on CPU, but 32 bit integer GPU division is actually on
            # CPU due to a placer bug.
            # TODO(irving): Make stricter once the placer bug is fixed.
            self.assertIn('Integer division by zero', str(e))
          else:
            # On the GPU, integer division by zero produces all bits set.
            # But apparently on some GPUs "all bits set" for 64 bit division
            # means 32 bits set, so we allow 0xffffffff as well.  This isn't
            # very portable, so we may need to expand this list if other GPUs
            # do different things.
            self.assertTrue(tf.test.is_gpu_available())
            self.assertIn(result, (-1, 0xff, 0xffffffff))


if __name__ == '__main__':
  tf.test.main()
