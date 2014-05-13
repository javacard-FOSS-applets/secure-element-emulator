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
package com.licel.jcardsim.crypto;

import javacard.framework.Util;
import javacard.security.CryptoException;
import javacard.security.ECPrivateKey;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;

/**
 * Implementation <code>ECPrivateKey</code> based
 * on BouncyCastle CryptoAPI
 * @see ECPrivateKey
 * @see ECPrivateKeyParameters
 */
public class ECPrivateKeyImpl extends ECKeyImpl implements ECPrivateKey {

    protected ByteContainer s = new ByteContainer();

    /**
     * Construct not-initialized ecc private key
     * @param keyType 
     * @param keySize key size it bits
     * @see javacard.security.KeyBuilder
     */
    public ECPrivateKeyImpl(byte keyType, short keySize) {
        super(keyType, keySize);
    }

    /**
     * Construct and initialize ecc key with ECPrivateKeyParameters.
     * Use in KeyPairImpl
     * @see javacard.security.KeyPair
     * @see ECPrivateKeyParameters
     * @param params key params from BouncyCastle API
     */
    public ECPrivateKeyImpl(ECPrivateKeyParameters params) {
        super(params);
        byte[] sBytes = params.getD().toByteArray();
        int byteLength = size / 8;
        if ((size % 8) != 0) ++byteLength;
        if (sBytes.length < byteLength) {
            byte[] bytesPadded = new byte[byteLength];
            Util.arrayCopy(sBytes, (short)0, bytesPadded, (short)(bytesPadded.length - sBytes.length), (short)sBytes.length);
            s.setBytes(bytesPadded);
        } else if ((sBytes.length > byteLength) && (sBytes[0] == 0) && ((sBytes[1] & 0x80) != 0)) {
            s.setBytes(sBytes, (short)1, (short)(sBytes.length - 1));
        } else {
            s.setBytes(sBytes);
        }
    }

    public void setS(byte[] buffer, short offset, short length) throws CryptoException {
        s.setBytes(buffer, offset, length);
    }

    public short getS(byte[] buffer, short offset) throws CryptoException {
        return s.getBytes(buffer, offset);
    }

    public boolean isInitialized() {
        return (isDomainParametersInitialized() && s.isInitialized());
    }

    public void clearKey() {
        super.clearKey();
        s.clear();
    }

    /**
     * Get <code>ECPrivateKeyParameters</code>
     * @return parameters for use with BouncyCastle API
     * @see ECPrivateKeyParameters
     */
    public CipherParameters getParameters() {
        if (!isInitialized()) {
            CryptoException.throwIt(CryptoException.UNINITIALIZED_KEY);
        }
        return new ECPrivateKeyParameters(s.getBigInteger(), getDomainParameters());
    }
}
