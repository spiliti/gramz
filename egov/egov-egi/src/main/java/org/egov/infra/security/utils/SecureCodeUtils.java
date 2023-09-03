/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.security.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.lang.RandomStringUtils;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

import static com.google.zxing.EncodeHintType.CHARACTER_SET;
import static com.google.zxing.EncodeHintType.ERROR_CORRECTION;
import static com.google.zxing.EncodeHintType.MARGIN;
import static com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.L;
import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static org.egov.infra.config.core.GlobalSettings.encoding;
import static org.egov.infra.utils.ImageUtils.PNG_EXTN;
import static org.egov.infra.utils.ImageUtils.PNG_FORMAT_NAME;
import static org.egov.infra.utils.ImageUtils.PNG_MIME_TYPE;

@Service
public class SecureCodeUtils {

    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 100;

    @Autowired
    @Qualifier("fileStoreService")
    private FileStoreService fileStoreService;

    public static File generateQRCode(String content) {
        return generateQRCode(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public static File generateQRCode(String content, int qrImgWidth, int qrImgHeight) {
        Path qrCodeFile;
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(CHARACTER_SET, encoding());
            hints.put(MARGIN, 1);
            hints.put(ERROR_CORRECTION, L);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix qrBitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrImgWidth, qrImgHeight, hints);
            int qrBitMatrixWidth = qrBitMatrix.getWidth();
            BufferedImage qrImage = new BufferedImage(qrBitMatrixWidth, qrBitMatrixWidth, BufferedImage.TYPE_INT_RGB);
            qrImage.createGraphics();
            Graphics2D qrImageGraphics = (Graphics2D) qrImage.getGraphics();
            qrImageGraphics.setColor(WHITE);
            qrImageGraphics.fillRect(0, 0, qrBitMatrixWidth, qrBitMatrixWidth);
            qrImageGraphics.setColor(BLACK);
            for (int i = 0; i < qrBitMatrixWidth; i++) {
                for (int j = 0; j < qrBitMatrixWidth; j++) {
                    if (qrBitMatrix.get(i, j)) {
                        qrImageGraphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            qrCodeFile = Files.createTempFile(RandomStringUtils.randomAlphabetic(5), PNG_EXTN);
            ImageIO.write(qrImage, PNG_FORMAT_NAME, qrCodeFile.toFile());
        } catch (WriterException | IOException e) {
            throw new ApplicationRuntimeException("Error occurred while generating QR Code", e);
        }

        return qrCodeFile.toFile();

    }

    public FileStoreMapper generateAndStoreQRCode(String content, int qrImgWidth, int qrImgHeight, String fileName, String moduleName) {
        return fileStoreService.store(generateQRCode(content, qrImgWidth, qrImgHeight), fileName, PNG_MIME_TYPE, moduleName);
    }

    public FileStoreMapper generateAndStoreQRCode(String content, String fileName, String moduleName) {
        return fileStoreService.store(generateQRCode(content), fileName, PNG_MIME_TYPE, moduleName);
    }
}
