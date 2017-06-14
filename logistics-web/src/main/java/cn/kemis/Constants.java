package cn.kemis;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-08-13
 */
public interface Constants {

    // 关键字类型
    String REPLACE_KEYWORDS_TYPE_GOODS = "goods";
    String REPLACE_KEYWORDS_TYPE_ADDRESS = "address";
    String REPLACE_KEYWORDS_TYPE_BIG_PACKAGE = "bigPackage";

    // 配送方式
    String SHIP_ORDER_DELIVERY_NORMAL = "普通快递";
    String SHIP_ORDER_DELIVERY_EMS = "EMS";

    // 导出文件类型
    String EXPORT_FILE_TYPE_DELIVERY = "delivery";
    String EXPORT_FILE_TYPE_ORDER = "order";
    String EXPORT_FILE_TYPE_DELIVERY_RESULT = "deliveryResult";
    String EXPORT_FILE_TYPE_SALARY = "salary";

    // 工序
    String WORK_PROCESS_CUTORDER = "cutOrder";
    String WORK_PROCESS_PICKINGGOODS = "pickingGoods";
    String WORK_PROCESS_PACKAGE = "package";
    String WORK_PROCESS_WRAPP = "wrapp";

    // 计件配置 key
    String WORK_PROCESS_PIECE_PRICE_CUTORDER = "workProcess.cutOrder.piece.price";  // 划单计价配置 key 单位(分)
    String WORK_PROCESS_PIECE_PRICE_PICKING = "workProcess.picking.piece.price";    // 捡货计价配置 key 单位(分)
    String WORK_PROCESS_PIECE_PRICE_WRAPP = "workProcess.wrapp.piece.price";        // 包装计价配置 key 单位(分)
    String WORK_PROCESS_PIECE_PRICE_PACKAGE = "workProcess.package.piece.price";    // 打包计价配置 key 单位(分)

    String AVATAR_PATH = "/avatar";     // 用户头像存储文件夹名称
    String AVATAR_SIZE_LARGE = "_l";    // 用户头像大尺寸后缀
    String AVATAR_SIZE_MIDDLE = "_m";   // 用户头像中尺寸后缀
    String AVATAR_SIZE_SMALL = "_s";    // 用户头像小尺寸后缀
    String AVATAR_IMAGE_SUFFIX = ".jpg";// 用户头像文件后缀

}
