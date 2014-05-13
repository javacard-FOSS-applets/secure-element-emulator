/*
 * Copyright 2011 Licel LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javacard.security;

import com.licel.jcardsim.crypto.RandomDataImpl;


/**
 *
 * The <code>RandomData</code> abstract class is the base class for random number generation.
 * Implementations of <code>RandomData</code>
 * algorithms must extend this class and implement all the abstract methods.
 */
public abstract class RandomData {

    /**
     * Utility pseudo-random number generation algorithms. The random number sequence
     * generated by this algorithm need not be the same even if seeded with the same
     * seed data.
     * <p> Even if a transaction is in progress, the update of the internal state
     * shall not participate in the transaction.
     */
    public static final byte ALG_PSEUDO_RANDOM = 1;
    /**
     * Cryptographically secure random number generation algorithms.
     */
    public static final byte ALG_SECURE_RANDOM = 2;

    /**
     * Protected constructor for subclassing.
     */
    protected RandomData() {
    }

    /**
     * Creates a <code>RandomData</code> instance of the selected algorithm.
     * The pseudo random <code>RandomData</code> instance's seed is initialized to a internal default value.
     * @param algorithm the desired random number algorithm. Valid codes listed in ALG_ .. constants above. See <A HREF="../../javacard/security/RandomData.html#ALG_PSEUDO_RANDOM"><CODE>ALG_PSEUDO_RANDOM</CODE></A>.
     * @return the <code>RandomData</code> object instance of the requested algorithm
     * @throws CryptoException with the following reason codes:<ul>
     * <li><code>CryptoException.NO_SUCH_ALGORITHM</code> if the requested algorithm is not supported.</ul>
     */
    public static final RandomData getInstance(byte algorithm)
            throws CryptoException {
        RandomData instance = null;
        switch (algorithm) {
            case ALG_PSEUDO_RANDOM:
            case ALG_SECURE_RANDOM:  // WARNING: ALG_SECURE_RANDOM implemented as PRNG!!!
                instance = new RandomDataImpl();
                break;
            default:
                CryptoException.throwIt((short) 3);
                break;
        }
        return instance;
    }

    /**
     * Generates random data.
     * @param buffer the output buffer
     * @param offset the offset into the output buffer
     * @param length the length of random data to generate
     * @throws CryptoException with the following reason codes:<ul>
     * <li><code>CryptoException.ILLEGAL_VALUE</code> if the <code>length</code> parameter is
     * zero.</ul>
     */
    public abstract void generateData(byte[] buffer, short offset, short length)
            throws CryptoException;

    /**
     * Seeds the random data generator.
     * @param buffer the input buffer
     * @param offset the offset into the input buffer
     * @param length the length of the seed data
     */
    public abstract void setSeed(byte[] buffer, short offset, short length);
}
